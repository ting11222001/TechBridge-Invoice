package io.techbridge.invoice.techbridge_invoice.query;

/**
 * @author Li-Ting Liao
 * @version 1.0
 * @since 02/2026
 */
public class RoleQuery {
    public static final String SELECT_ROLE_BY_NAME_QUERY = "SELECT * FROM Roles WHERE name = :roleName";
    public static final String INSERT_ROLE_TO_USER_QUERY = "INSERT INTO UserRoles (user_id, role_id) VALUES (:userId, :roleId)";
}
