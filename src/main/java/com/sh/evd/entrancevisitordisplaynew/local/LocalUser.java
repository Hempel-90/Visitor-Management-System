package com.sh.evd.entrancevisitordisplaynew.local;

//region Imports--------------------------------------------------------------------------------------------------------

/* Entwicklungsversion von AppUserPrincipal und UserDetails.
 * Stellt Testbenutzer mit vordefinierten Anmeldeinformationen bereit.
 */

import com.sh.evd.entrancevisitordisplaynew.security.AppUserInfo;
import com.sh.evd.entrancevisitordisplaynew.security.AppUserPrincipal;
import com.sh.evd.entrancevisitordisplaynew.security.UserId;
import lombok.Getter;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.ZoneId;
import java.util.*;

import static java.util.Objects.requireNonNull;

//endregion Imports-----------------------------------------------------------------------------------------------------

//region LocalUser------------------------------------------------------------------------------------------------------

/* Implementierung von AppUserPrincipal & UserDetails für Entwicklungsumgebungen.
 * Kann über den Builder LocalUserBuilder konfiguriert werden.
 */

final class LocalUser implements AppUserPrincipal, UserDetails {

    //region Fields-----------------------------------------------------------------------------------------------------
    private final AppUserInfo appUser;
    private final Set<GrantedAuthority> authorities;
    private final String password;
    //endregion Fields--------------------------------------------------------------------------------------------------

    // Konstruktor
    LocalUser(AppUserInfo appUser, Collection<GrantedAuthority> authorities, String password) {
        this.appUser = requireNonNull(appUser);
        this.authorities = Set.copyOf(authorities);
        this.password = requireNonNull(password);
    }

    //region AppUserPrincipal & UserDetails Methoden--------------------------------------------------------------------

    @Override
    public AppUserInfo getAppUser() {
        return appUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return appUser.getPreferredUsername();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LocalUser user) {
            return this.appUser.getUserId().equals(user.appUser.getUserId());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.appUser.getUserId().hashCode();
    }

    //endregion AppUserPrincipal & UserDetails Methoden-----------------------------------------------------------

    //region Builder---------------------------------------------------------------------------------------------------
    public static LocalUserBuilder builder() {
        return new LocalUserBuilder();
    }

    static final class LocalUserBuilder {

        // Password Encoder
        private static final PasswordEncoder PASSWORD_ENCODER =
                PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // Builder-Felder
        private @Nullable UserId userId;
        private @Nullable String preferredUsername;
        private @Nullable String fullName;
        private @Nullable String email;
        private @Nullable String profileUrl;
        private @Nullable String pictureUrl;
        private ZoneId zoneInfo = ZoneId.systemDefault();
        private Locale locale = Locale.getDefault();
        private List<GrantedAuthority> authorities = Collections.emptyList();
        private @Nullable String password;

        //region Setter-Methoden---------------------------------------------------------------------------------------

        public LocalUserBuilder userId(UserId userId) {
            this.userId = requireNonNull(userId);
            return this;
        }

        public LocalUserBuilder preferredUsername(String preferredUsername) {
            this.preferredUsername = requireNonNull(preferredUsername);
            return this;
        }

        public LocalUserBuilder fullName(@Nullable String fullName) {
            this.fullName = fullName;
            return this;
        }

        public LocalUserBuilder email(@Nullable String email) {
            this.email = email;
            return this;
        }

        public LocalUserBuilder profileUrl(@Nullable String profileUrl) {
            this.profileUrl = profileUrl;
            return this;
        }

        public LocalUserBuilder pictureUrl(@Nullable String pictureUrl) {
            this.pictureUrl = pictureUrl;
            return this;
        }

        public LocalUserBuilder zoneInfo(ZoneId zoneInfo) {
            this.zoneInfo = requireNonNull(zoneInfo);
            return this;
        }

        public LocalUserBuilder locale(Locale locale) {
            this.locale = requireNonNull(locale);
            return this;
        }

        public LocalUserBuilder roles(String... roles) {
            this.authorities = new ArrayList<>(roles.length);
            for (var role : roles) {
                this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return this;
        }

        public LocalUserBuilder roles(Collection<String> roles) {
            this.authorities = new ArrayList<>(roles.size());
            for (var role : roles) {
                this.authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
            }
            return this;
        }

        public LocalUserBuilder authorities(String... authorities) {
            return authorities(AuthorityUtils.createAuthorityList(authorities));
        }

        public LocalUserBuilder authorities(Collection<? extends GrantedAuthority> authorities) {
            this.authorities = new ArrayList<>(authorities);
            return this;
        }

        public LocalUserBuilder password(String password) {
            this.password = requireNonNull(password);
            return this;
        }

        //endregion Setter-Methoden-------------------------------------------------------------------------------------

        // Baut den LocalUser
        public LocalUser build() {
            if (preferredUsername == null) {
                throw new IllegalStateException("Preferred username must be set before building the user");
            }
            if (password == null) {
                throw new IllegalStateException("Password must be set before building the user");
            }
            var encodedPassword = PASSWORD_ENCODER.encode(password);
            if (userId == null) userId = UserId.of(UUID.randomUUID().toString());
            if (fullName == null) fullName = preferredUsername;

            return new LocalUser(new LocalUserInfo(
                    userId, preferredUsername, fullName, profileUrl, pictureUrl, email, zoneInfo, locale
            ), authorities, encodedPassword);
        }
    }
    //endregion Builder-------------------------------------------------------------------------------------------------

}

//endregion LocalUser---------------------------------------------------------------------------------------------------
