package sinkj1.library.config;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.RestTemplate;
import sinkj1.library.domain.CustomObjectIdentity;
import sinkj1.library.security.jwt.TokenProvider;
import sinkj1.library.service.HttpClient;
import sinkj1.library.service.dto.CheckPermissionDto;

@Configuration
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final Log logger = LogFactory.getLog(getClass());

   //private final AclService aclService;

    private final TokenProvider tokenProvider;

    private final HttpClient<CheckPermissionDto> httpClient;

    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();

    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();

    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

    private PermissionFactory permissionFactory = new DefaultPermissionFactory();

    public AclPermissionEvaluator(TokenProvider tokenProvider, HttpClient<CheckPermissionDto> httpClient) {
        this.tokenProvider = tokenProvider;
        //   this.aclService = aclService;
        this.httpClient = httpClient;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        }

        if(domainObject instanceof Optional){
            domainObject = ((Optional<?>) domainObject).get();
        }

        ObjectIdentity objectIdentity = this.objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);
        String token = tokenProvider.createToken(authentication, false);
        String value = httpClient.post("http://localhost:8085/api/permission/check", new CheckPermissionDto(new CustomObjectIdentity(objectIdentity.getIdentifier(),objectIdentity.getType()), permission));

        return Boolean.valueOf(value);//checkPermission(authentication,objectIdentity,permission);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType,
                                 Object permission) {
        ObjectIdentity objectIdentity = this.objectIdentityGenerator.createObjectIdentity(targetId, targetType);
        return checkPermission(authentication, objectIdentity, permission);
    }

    private boolean checkPermission(Authentication authentication, ObjectIdentity oid, Object permission) {
        // Obtain the SIDs applicable to the principal
        List<Sid> sids = this.sidRetrievalStrategy.getSids(authentication);
        List<Permission> requiredPermission = resolvePermission(permission);
        this.logger.debug(LogMessage.of(() -> "Checking permission '" + permission + "' for object '" + oid + "'"));
        try {
//2
            Acl acl = null;//this.aclService.readAclById(oid, sids);
            if (acl.isGranted(requiredPermission, sids, false)) {
                this.logger.debug("Access is granted");
                return true;
            }
            this.logger.debug("Returning false - ACLs returned, but insufficient permissions for this principal");
        }
        catch (NotFoundException nfe) {
            this.logger.debug("Returning false - no ACLs apply for this principal");
        }
        return false;
    }




    List<Permission> resolvePermission(Object permission) {
        if (permission instanceof Integer) {
            return Arrays.asList(this.permissionFactory.buildFromMask((Integer) permission));
        }
        if (permission instanceof Permission) {
            return Arrays.asList((Permission) permission);
        }
        if (permission instanceof Permission[]) {
            return Arrays.asList((Permission[]) permission);
        }
        if (permission instanceof String) {
            String permString = (String) permission;
            Permission p = buildPermission(permString);
            if (p != null) {
                return Arrays.asList(p);
            }
        }
        throw new IllegalArgumentException("Unsupported permission: " + permission);
    }

    private Permission buildPermission(String permString) {
        try {
            return this.permissionFactory.buildFromName(permString);
        }
        catch (IllegalArgumentException notfound) {
            return this.permissionFactory.buildFromName(permString.toUpperCase(Locale.ENGLISH));
        }
    }

    public void setObjectIdentityRetrievalStrategy(ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy) {
        this.objectIdentityRetrievalStrategy = objectIdentityRetrievalStrategy;
    }

    public void setObjectIdentityGenerator(ObjectIdentityGenerator objectIdentityGenerator) {
        this.objectIdentityGenerator = objectIdentityGenerator;
    }

    public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

    public void setPermissionFactory(PermissionFactory permissionFactory) {
        this.permissionFactory = permissionFactory;
    }

}
