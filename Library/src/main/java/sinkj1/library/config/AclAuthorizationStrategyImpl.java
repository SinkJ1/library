package sinkj1.library.config;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.acls.domain.*;
import org.springframework.security.acls.model.Acl;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;


public class AclAuthorizationStrategyImpl /*implements AclAuthorizationStrategy*/ {

 /*   private final GrantedAuthority gaGeneralChanges;

    private final GrantedAuthority gaModifyAuditing;

    private final GrantedAuthority gaTakeOwnership;

    private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

    public AclAuthorizationStrategyImpl(GrantedAuthority... auths) {
        Assert.isTrue(auths != null && (auths.length == 3 || auths.length == 1),
            "One or three GrantedAuthority instances required");
        if (auths.length == 3) {
            this.gaTakeOwnership = auths[0];
            this.gaModifyAuditing = auths[1];
            this.gaGeneralChanges = auths[2];
        }
        else {
            this.gaTakeOwnership = auths[0];
            this.gaModifyAuditing = auths[0];
            this.gaGeneralChanges = auths[0];
        }
    }



    private GrantedAuthority getRequiredAuthority(int changeType) {
        if (changeType == CHANGE_AUDITING) {
            return this.gaModifyAuditing;
        }
        if (changeType == CHANGE_GENERAL) {
            return this.gaGeneralChanges;
        }
        if (changeType == CHANGE_OWNERSHIP) {
            return this.gaTakeOwnership;
        }
        throw new IllegalArgumentException("Unknown change type");
    }

    protected Sid createCurrentUser(Authentication authentication) {
        return new PrincipalSid(authentication);
    }

    public void setSidRetrievalStrategy(SidRetrievalStrategy sidRetrievalStrategy) {
        Assert.notNull(sidRetrievalStrategy, "SidRetrievalStrategy required");
        this.sidRetrievalStrategy = sidRetrievalStrategy;
    }

    @Override
    public void securityCheck(Acl acl, int changeType) {
        if ((SecurityContextHolder.getContext() == null)
            || (SecurityContextHolder.getContext().getAuthentication() == null)
            || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AccessDeniedException("Authenticated principal required to operate with ACLs");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Sid currentUser = createCurrentUser(authentication);
        if (currentUser.equals(acl.getOwner())
            && ((changeType == CHANGE_GENERAL) || (changeType == CHANGE_OWNERSHIP))) {
            return;
        }

        // Iterate this principal's authorities to determine right
        Set<String> authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        if (acl.getOwner() instanceof GrantedAuthoritySid
            && authorities.contains(((GrantedAuthoritySid) acl.getOwner()).getGrantedAuthority())) {
            return;
        }

        GrantedAuthority requiredAuthority = getRequiredAuthority(changeType);

        if (authorities.contains(requiredAuthority.getAuthority())) {
            return;
        }

        List<Sid> sids = this.sidRetrievalStrategy.getSids(authentication);
        if (acl.isGranted(Arrays.asList(BasePermission.ADMINISTRATION), sids, false)) {
            return;
        }

        throw new AccessDeniedException(
            "Principal does not have required ACL permissions to perform requested operation");
    }*/
}
