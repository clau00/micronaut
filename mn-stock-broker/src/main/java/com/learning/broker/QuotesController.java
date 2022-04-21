package com.learning.broker;

import com.learning.broker.api.RestApiResponse;
import com.learning.broker.data.InMemoryStore;
import com.learning.broker.persistence.jpa.QuotesRepository;
import com.learning.broker.persistence.model.QuoteDTO;
import com.learning.broker.persistence.model.QuoteEntity;
import com.learning.broker.persistence.model.SymbolEntity;
import com.learning.broker.wallet.error.CustomError;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@Secured(SecurityRule.IS_ANONYMOUS)
@Controller("/quotes")
public class QuotesController {

    private final InMemoryStore store;
    private final QuotesRepository quotesRepository;

    public QuotesController(InMemoryStore store, QuotesRepository quotesRepository) {
        this.store = store;
        this.quotesRepository = quotesRepository;
    }

    @Operation(summary = "Returns a quote for the given symbol")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get("/{symbol}")
    public HttpResponse<RestApiResponse> getQuotes(@PathVariable String symbol) {
        final Optional<Quote> maybeQuote = store.fetchQuote(symbol);
        if (maybeQuote.isEmpty()) {
            final CustomError notFound = CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote for symbol not available")
                    .path("/quotes/" + symbol)
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(maybeQuote.get());
    }

    @Get("/jpa")
    public List<QuoteEntity> getAllQuotes() {
        return quotesRepository.findAll();
    }

    @Operation(summary = "Returns a quote for the given symbol. Fetched from database via JPA")
    @ApiResponse(
            content = @Content(mediaType = MediaType.APPLICATION_JSON)
    )
    @ApiResponse(responseCode = "400", description = "Invalid symbol specified")
    @Tag(name = "quotes")
    @Get("/{symbol}/jpa")
    public HttpResponse<RestApiResponse> getQuotesViaJPA(@PathVariable String symbol) {
        final Optional<QuoteEntity> maybeQuote = quotesRepository.findBySymbol(new SymbolEntity(symbol));
        if (maybeQuote.isEmpty()) {
            final CustomError notFound = CustomError.builder()
                    .status(HttpStatus.NOT_FOUND.getCode())
                    .error(HttpStatus.NOT_FOUND.name())
                    .message("quote for symbol not available in db")
                    .path("/quotes/" + symbol + "/jpa")
                    .build();
            return HttpResponse.notFound(notFound);
        }
        return HttpResponse.ok(maybeQuote.get());
    }

    @Get("/jpa/ordered/desc")
    public List<QuoteDTO> orderedDesc() {
        return quotesRepository.listOrderByVolumeDesc();
    }

    @Get("/jpa/ordered/asc")
    public List<QuoteDTO> orderedAsc() {
        return quotesRepository.listOrderByVolumeAsc();
    }
}
