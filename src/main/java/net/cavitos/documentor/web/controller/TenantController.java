package net.cavitos.documentor.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.cavitos.documentor.domain.exception.ValidationException;
import net.cavitos.documentor.domain.model.Tenant;
import net.cavitos.documentor.service.TenantService;
import net.cavitos.documentor.web.model.request.NewTenantRequest;
import net.cavitos.documentor.web.model.request.UpdateTenantRequest;
import net.cavitos.documentor.web.model.response.ResourceResponse;
import net.cavitos.documentor.web.validator.tenant.NewTenantRequestValidator;
import net.cavitos.documentor.web.validator.tenant.UpdateTenantRequestValidator;

import static net.cavitos.documentor.web.controller.ControllerRoute.TENANTS_ROUTE;

@RestController
@RequestMapping(ControllerRoute.TENANTS_ROUTE)
public class TenantController extends BaseController {
    
    @Autowired
    private TenantService tenantService;

    @Autowired
    private NewTenantRequestValidator newTenantRequestValidator;

    @Autowired
    private UpdateTenantRequestValidator updateTenantRequestValidator;

    @RequestMapping
    public ResponseEntity<Page<Tenant>> getTenants(@RequestParam(defaultValue = DEFAULT_SIZE) final int size,
                                                   @RequestParam(defaultValue = DEFAULT_PAGE) final int page) {

        var tenantPage = tenantService.getTenants(size, page);        
        return new ResponseEntity<>(tenantPage, HttpStatus.OK);
    }

    @GetMapping("/{tenantId}")
    public ResponseEntity<ResourceResponse<Tenant>> getTenantById(@PathVariable final String tenantId) {

        var tenant = tenantService.getTenantById(tenantId);

        var response = new ResourceResponse<>(tenant, buildSelf(tenant.getTenantId()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResourceResponse<Tenant>> newTenant(@RequestBody final NewTenantRequest newTenantRequest) {

        var errors = buildErrorObject(newTenantRequest);
        newTenantRequestValidator.validate(newTenantRequest, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        var storedTenant = tenantService.newTenant(newTenantRequest);

        var response = new ResourceResponse<>(storedTenant, buildSelf(storedTenant.getTenantId()));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{tenantId}")
    public ResponseEntity<ResourceResponse<Tenant>> updateTenant(@PathVariable final String tenantId,     
                                                                    @RequestBody final UpdateTenantRequest tenant) {

        var errors = buildErrorObject(tenant);
        updateTenantRequestValidator.validate(tenant, errors);

        if (errors.hasErrors()) {

            throw new ValidationException(errors);
        }

        var updatedTenant = tenantService.updateTenant(tenantId, tenant);

        var response = new ResourceResponse<>(updatedTenant, buildSelf(updatedTenant.getTenantId()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{tenantId}")
    public ResponseEntity<Void> deleteTenant(@PathVariable final String tenantId) {
        
        tenantService.deleteTenant(tenantId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // ---------------------------------------------------------------------------------------------------------

    private String buildSelf(String self) {

        return new StringBuilder()
            .append(TENANTS_ROUTE)
            .append("/")
            .append(self)
            .toString();
    }
}
