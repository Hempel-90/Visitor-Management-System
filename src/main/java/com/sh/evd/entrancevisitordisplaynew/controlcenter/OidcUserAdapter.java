package com.sh.evd.entrancevisitordisplaynew.controlcenter;

//region Imports--------------------------------------------------------------------------------------------------------

/*
 * Adapter für OIDC-Benutzer, der Spring Security OidcUser mit
 * der eigenen AppUserPrincipal/AppUserInfo Schnittstelle verbindet.
 */

import com.sh.evd.entrancevisitordisplaynew.security.AppUserInfo;
import com.sh.evd.entrancevisitordisplaynew.security.AppUserPrincipal;
import com.sh.evd.entrancevisitordisplaynew.security.UserId;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import static java.util.Objects.requireNonNull;


//endregion Imports-----------------------------------------------------------------------------------------------------

//region OidcUserAdapter------------------------------------------------------------------------------------------------

/*
 *  Adapter-Klasse für OIDC-Benutzer:
 *  - Wrappet ein OidcUser Objekt
 *  - Implementiert AppUserPrincipal
 *  - Bietet konsistenten Zugriff auf AppUserInfo
 */
final class OidcUserAdapter implements OidcUser, AppUserPrincipal {

    private final OidcUser delegate;
    private final AppUserInfo appUserInfo;

    // Konstruktor
    OidcUserAdapter(OidcUser oidcUser) {
        this.delegate = requireNonNull(oidcUser);
        this.appUserInfo = createAppUserInfo(oidcUser);
    }

    // AppUserInfo erstellen
    private static AppUserInfo createAppUserInfo(OidcUser oidcUser) {
        return new AppUserInfo() {
            private final UserId userId = UserId.of(oidcUser.getSubject());
            private final String preferredUsername = requireNonNull(oidcUser.getPreferredUsername());
            private final String fullName = requireNonNull(oidcUser.getFullName());
            private final ZoneId zoneId = parseZoneInfo(oidcUser.getZoneInfo());
            private final Locale locale = parseLocale(oidcUser.getLocale());

            @Override
            public UserId getUserId() { return userId; }

            @Override
            public String getPreferredUsername() { return preferredUsername; }

            @Override
            public String getFullName() { return fullName; }

            @Override
            public @Nullable String getProfileUrl() { return oidcUser.getProfile(); }

            @Override
            public @Nullable String getPictureUrl() { return oidcUser.getPicture(); }

            @Override
            public @Nullable String getEmail() { return oidcUser.getEmail(); }

            @Override
            public ZoneId getZoneId() { return zoneId; }

            @Override
            public Locale getLocale() { return locale; }
        };
    }

    // ZoneId parsen
    static ZoneId parseZoneInfo(@Nullable String zoneInfo) {
        if (zoneInfo == null) return ZoneId.systemDefault();
        try {
            return ZoneId.of(zoneInfo);
        } catch (DateTimeException e) {
            return ZoneId.systemDefault();
        }
    }

    // Locale parsen
    static Locale parseLocale(@Nullable String locale) {
        if (locale == null) return Locale.getDefault();
        return Locale.forLanguageTag(locale);
    }

    //region AppUserPrincipal-------------------------------------------------------------------------------------------

    @Override
    public AppUserInfo getAppUser() { return appUserInfo; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return delegate.getAuthorities(); }

    @Override
    public String getName() { return delegate.getName(); }

    //endregion AppUserPrincipal----------------------------------------------------------------------------------------

    //region OidcUser Delegation----------------------------------------------------------------------------------------

    @Override
    public Map<String, Object> getClaims() { return delegate.getClaims(); }

    @Override
    public OidcUserInfo getUserInfo() { return delegate.getUserInfo(); }

    @Override
    public OidcIdToken getIdToken() { return delegate.getIdToken(); }

    @Override
    public Map<String, Object> getAttributes() { return delegate.getAttributes(); }

    //endregion OidcUser Delegation-------------------------------------------------------------------------------------
}

//endregion OidcUserAdapter---------------------------------------------------------------------------------------------
