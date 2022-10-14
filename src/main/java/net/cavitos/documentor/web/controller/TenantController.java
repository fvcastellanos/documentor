package net.cavitos.documentor.web.controller;

import net.cavitos.documentor.domain.web.Tenant;
import net.cavitos.documentor.security.service.UserService;
import net.cavitos.documentor.service.TenantService;
import net.cavitos.documentor.transformer.TenantTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.security.Principal;

@RestController
@RequestMapping("/tenants")
public class TenantController extends BaseController {
    
    private final TenantService tenantService;

    @Autowired
    public TenantController(final TenantService tenantService,
                            final UserService userService) {

        super(userService);
        this.tenantService = tenantService;
    }

    @GetMapping
    public ResponseEntity<Page<Tenant>> getTenants(@RequestParam(defaultValue = DEFAULT_SIZE) final int size,
                                                   @RequestParam(defaultValue = DEFAULT_PAGE) final int page) {

        final var tenantPage = tenantService.getTenants(size, page);

        final var tenants = tenantPage.stream()
                .map(TenantTransformer::toWeb)
                .toList();

        final var response = new PageImpl<>(tenants, Pageable.ofSize(size), tenantPage.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getTenantById(@PathVariable final String id) {

        var tenant = tenantService.getTenantById(id);

        var response = TenantTransformer.toWeb(tenant);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Tenant> newTenant(@RequestBody @Valid final Tenant tenant) {

        final var storedTenant = tenantService.newTenant(tenant);

        final var response = TenantTransformer.toWeb(storedTenant);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tenant> updateTenant(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                               @RequestBody @Valid final Tenant tenant) {

        var updatedTenant = tenantService.updateTenant(id, tenant);

        var response = TenantTransformer.toWeb(updatedTenant);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
