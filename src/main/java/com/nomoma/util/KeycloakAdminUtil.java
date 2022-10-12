package com.nomoma.util;

import java.util.Collections;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.nomoma.member.model.dto.MemberDto;

import lombok.RequiredArgsConstructor;

//-> service + refactoring
@RequiredArgsConstructor
@Component
public class KeycloakAdminUtil {
    private final Keycloak keycloak;

    @Qualifier("keycloakAdmin")
    public Keycloak keycloakAdmin() {
        return keycloak;
    }

    //로직 분리하기
    public boolean addUser(MemberDto memberDto) {
        final UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setUsername(memberDto.getName());

        final CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(memberDto.getPassword());
        userRepresentation.setCredentials(Collections.singletonList(credentialRepresentation));
        userRepresentation.setEnabled(true);

        final boolean isUserEmpty = keycloak.realm(memberDto.getRealm()).users().search(memberDto.getName()).isEmpty();

        if (isUserEmpty) {
            keycloak.realm(memberDto.getRealm())
                    .users()
                    .create(userRepresentation)
                    ;
            return true;
        } else {
            return false;
        }
    }
}
