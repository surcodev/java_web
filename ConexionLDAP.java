package com.mycompany.conexionldap;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;

import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class ConexionLDAP {

    private static final String LDAP_URL = "ldap://192.168.100.186:389"; // Reemplaza con la IP de tu servidor AD
    private static final String DOMAIN = "itp.gob.pe"; // Reemplaza con tu dominio correcto
    private static final String BASE_DN = "DC=itp,DC=gob,DC=pe"; // Separadores corregidos

    /**
     * Método para autenticar un usuario en Active Directory
     */
    public static boolean authenticate(String username, String password) {
        String userPrincipalName = username + "@" + DOMAIN;

        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, userPrincipalName); // Usa el usuario que se quiere autenticar
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.REFERRAL, "ignore"); // Evita errores de referencias no manejadas

        try {
            LdapContext ctx = new InitialLdapContext(env, null);
            System.out.println("✅ Autenticación exitosa para el usuario: " + username);
            ctx.close();
            return true;
        } catch (NamingException e) {
            System.out.println("❌ Error de autenticación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Método para buscar un usuario en Active Directory
     */
    public static void searchUser(String searchUsername) {
    Hashtable<String, String> env = new Hashtable<>();
    env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    env.put(Context.PROVIDER_URL, LDAP_URL);
    env.put(Context.SECURITY_AUTHENTICATION, "simple");
    env.put(Context.SECURITY_PRINCIPAL, "rquispe@" + DOMAIN); // Usuario con permisos de consulta
    env.put(Context.SECURITY_CREDENTIALS, "brokenB19");
    env.put(Context.REFERRAL, "follow"); // Opciones: "ignore", "follow"

    try {
        DirContext ctx = new InitialDirContext(env);
        SearchControls controls = new SearchControls();
        controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        // Limita los atributos solicitados para evitar referencias innecesarias
        controls.setReturningAttributes(new String[] { "cn", "sAMAccountName", "mail" });

        String searchFilter = "(sAMAccountName=" + searchUsername + ")";
        NamingEnumeration<SearchResult> results = ctx.search(BASE_DN, searchFilter, controls);

        if (!results.hasMore()) {
            System.out.println("🔍 Usuario no encontrado: " + searchUsername);
        }

        while (results.hasMore()) {
            SearchResult result = results.next();
            Attributes attrs = result.getAttributes();
            Attribute cn = attrs.get("cn");
            if (cn != null) {
                System.out.println("✅ Usuario encontrado: " + cn.get());
            }
        }

        ctx.close();
    } catch (NamingException e) {
        System.out.println("❌ Error en la búsqueda: " + e.getMessage());
    }
}


    public static void main(String[] args) {
        // Prueba de autenticación
        authenticate("rquispe", "brokenB19");

        // Búsqueda de usuario
        searchUser("administrador");
    }
}
