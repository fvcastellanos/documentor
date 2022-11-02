package net.cavitos.documentor.web.controller;

import net.cavitos.documentor.domain.web.Tenant;
import net.cavitos.documentor.domain.web.UpdateUser;
import net.cavitos.documentor.domain.web.User;
import net.cavitos.documentor.security.service.UserService;
import net.cavitos.documentor.service.TenantService;
import net.cavitos.documentor.transformer.TenantTransformer;
import net.cavitos.documentor.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    @PreAuthorize("hasAuthority('SCOPE_admin')")
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
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Tenant> getTenantById(@PathVariable final String id) {

        var tenant = tenantService.getTenantById(id);

        var response = TenantTransformer.toWeb(tenant);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Tenant> newTenant(@RequestBody @Valid final Tenant tenant) {

        final var storedTenant = tenantService.newTenant(tenant);

        final var response = TenantTransformer.toWeb(storedTenant);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Tenant> updateTenant(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                               @RequestBody @Valid final Tenant tenant) {

        var updatedTenant = tenantService.updateTenant(id, tenant);

        var response = TenantTransformer.toWeb(updatedTenant);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // --------------------------------------------------------------------------------------------------------

    @GetMapping("/{id}/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Page<User>> getUsers(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                               @RequestParam(defaultValue = DEFAULT_SIZE) final int size,
                                               @RequestParam(defaultValue = DEFAULT_PAGE) final int page) {

        final var userPage = tenantService.findUsersByTenant(id, size, page);

        final var users = userPage.stream()
                .map(userDocument -> UserTransformer.toWeb(id, userDocument))
                .toList();

        final var response = new PageImpl<>(users, Pageable.ofSize(size), userPage.getTotalElements());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}/users/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<User> getUserById(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                            @PathVariable @NotEmpty @Size(max = 50) final String userId) {

        final var userDocument = tenantService.getUserById(id, userId);
        final var response = UserTransformer.toWeb(id, userDocument);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{id}/users")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<User> addUser(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                        @RequestBody @Valid final User user) {

        final var userDocument = tenantService.addUser(id, user);
        final var response = UserTransformer.toWeb(id, userDocument);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/users/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<User> updateUser(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                           @PathVariable @NotEmpty @Size(max = 50) final String userId,
                                           @RequestBody @Valid final UpdateUser updateUser) {

        final var userDocument = tenantService.updateUser(id, userId, updateUser);
        final var response = UserTransformer.toWeb(id, userDocument);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}/users/{userId}")
    @PreAuthorize("hasAuthority('SCOPE_admin')")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotEmpty @Size(max = 50) final String id,
                                           @PathVariable @NotEmpty @Size(max = 50) final String userId) {

        tenantService.deleteUser(id, userId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
