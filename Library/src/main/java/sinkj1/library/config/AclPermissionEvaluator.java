package sinkj1.library.config;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.domain.DefaultPermissionFactory;
import org.springframework.security.acls.domain.ObjectIdentityRetrievalStrategyImpl;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.acls.model.ObjectIdentityRetrievalStrategy;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import sinkj1.library.domain.CustomObjectIdentity;
import sinkj1.library.security.jwt.TokenProvider;
import sinkj1.library.service.HttpClient;
import sinkj1.library.service.dto.CheckPermissionDto;

@Configuration
public class AclPermissionEvaluator implements PermissionEvaluator {

    private final Log logger = LogFactory.getLog(getClass());

    private final TokenProvider tokenProvider;

    private final HttpClient<CheckPermissionDto> httpClient;

    private ObjectIdentityRetrievalStrategy objectIdentityRetrievalStrategy = new ObjectIdentityRetrievalStrategyImpl();

    private ObjectIdentityGenerator objectIdentityGenerator = new ObjectIdentityRetrievalStrategyImpl();

    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

    private PermissionFactory permissionFactory = new DefaultPermissionFactory();

    public AclPermissionEvaluator(TokenProvider tokenProvider, HttpClient<CheckPermissionDto> httpClient) {
        this.tokenProvider = tokenProvider;
        this.httpClient = httpClient;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object domainObject, Object permission) {
        if (domainObject == null) {
            return false;
        }

        if (domainObject instanceof Optional) {
            domainObject = ((Optional<?>) domainObject).get();
        }

        ObjectIdentity objectIdentity = this.objectIdentityRetrievalStrategy.getObjectIdentity(domainObject);
        String token = tokenProvider.createToken(authentication, false);
        String value = httpClient.post(
            "https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/permission/check",
            new CheckPermissionDto(new CustomObjectIdentity(objectIdentity.getIdentifier(), objectIdentity.getType()), permission),
            token
        );

        return Boolean.valueOf(value);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        ObjectIdentity objectIdentity = this.objectIdentityGenerator.createObjectIdentity(targetId, targetType);
        String token = tokenProvider.createToken(authentication, false);
        String value = httpClient.post(
            "https://practice.sqilsoft.by/internship/yury_sinkevich/acl/api/permission/check",
            new CheckPermissionDto(new CustomObjectIdentity(objectIdentity.getIdentifier(), objectIdentity.getType()), permission),
            token
        );

        return Boolean.valueOf(value);
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
