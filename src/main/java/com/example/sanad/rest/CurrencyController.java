package com.example.sanad.rest;

import com.example.sanad.entity.Currency;
import com.example.sanad.rest.dto.CurrencyDTO;
import com.example.sanad.rest.dto.CurrencyExchangeRateDTO;
import com.example.sanad.rest.dto.NewCurrencyDTO;
import com.example.sanad.rest.mapper.CurrencyMapper;
import com.example.sanad.rest.util.BaseResponse;
import com.example.sanad.rest.util.PaginatedResponse;
import com.example.sanad.services.CurrencyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.example.sanad.rest.util.httpUtil.buildResponse;
import static com.example.sanad.rest.util.httpUtil.buildResponseEntity;

@RestController
@RequestMapping("/api/v1/currency")
public class CurrencyController {
    private final CurrencyService currencyService;
    private final CurrencyMapper currencyMapper;

    public CurrencyController(CurrencyService currencyService, CurrencyMapper currencyMapper) {
        this.currencyService = currencyService;
        this.currencyMapper = currencyMapper;
    }

    // Get a list of currencies
    @GetMapping
    public ResponseEntity<PaginatedResponse<List<CurrencyDTO>>> getAllCurrencies(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name,asc") String sort,
            @RequestParam(value = "searchQuery", required = false) String searchQuery
    ) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by(sort));
        Page<Currency> currencyPage = currencyService.listAllCurrencies(searchQuery, pageable);

        return buildResponse(
                HttpStatus.OK,
                "Currencies retrieved successfully",
                currencyPage,
                currencyPage.getContent().stream().map(currencyMapper::toDto).toList()
        );
    }

    // Get exchange rates for a currency
    @GetMapping("/{currencyCode}/exchange-rates")
    public ResponseEntity<BaseResponse<CurrencyExchangeRateDTO>> getExchangeRates(@PathVariable String currencyCode) {
        return buildResponseEntity(
                HttpStatus.OK,
                "Exchange rates retrieved successfully",
                currencyMapper.toExchangeRateDto(currencyService.getCurrencyByCode(currencyCode))
        );
    }

    // Add a new currency
    @PostMapping
    public ResponseEntity<BaseResponse<CurrencyDTO>> addCurrency(@RequestBody NewCurrencyDTO currency) {
        return buildResponseEntity(
                HttpStatus.CREATED,
                "Currency added successfully",
                currencyMapper.toDto(currencyService.addCurrency(currencyMapper.toEntity(currency)))
        );
    }
}
