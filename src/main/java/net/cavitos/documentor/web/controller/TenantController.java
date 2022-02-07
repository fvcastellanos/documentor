package net.cavitos.documentor.web.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.model.Tenant;
import net.cavitos.documentor.domain.response.NewResourceResponse;
import net.cavitos.documentor.service.TenantService;
import net.cavitos.documentor.web.validator.TenantValidator;

@RestController
@RequestMapping("/tenants")
public class TenantController extends BaseController {
    
    @Autowired
    private TenantService tenantService;

    @Autowired
    private TenantValidator tenantValidator;

    @RequestMapping
    public ResponseEntity<Page<Tenant>> getTenants(@RequestParam(defaultValue = DEFAULT_SIZE) final int size,
                                                   @RequestParam(defaultValue = DEFAULT_PAGE) final int page) {

        var tenantPage = tenantService.getTenants(size, page);        
        return new ResponseEntity<>(tenantPage, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<NewResourceResponse<Tenant>> newTenant(@RequestBody final Tenant tenant) {

        var errors = buildErrorObject(tenant);
        tenantValidator.validate(tenant, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        var storedTenant = tenantService.newTenant(tenant);

        var response = new NewResourceResponse<>(storedTenant, "/tenants/" + storedTenant.getTenantId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<NewResourceResponse<Tenant>> updateTenant(@RequestBody final Tenant tenant) {

        var errors = buildErrorObject(tenant);
        tenantValidator.validate(tenant, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        var updatedTenant = tenantService.updateTenant(tenant);

        var response = new NewResourceResponse<>(updatedTenant, "/tenants/" + updatedTenant.getTenantId());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
