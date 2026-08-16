/*
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime expiresAt;
    private String loginCode;
    private LocalDateTime loginCodeExpiresAt;

*/

ALTER TABLE user
    ADD COLUMN is_active BOOLEAN not null DEFAULT true,
    ADD COLUMN last_login DATETIME NULL DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN expires_at DATETIME null,
    ADD COLUMN login_code CHAR(6) null,
    ADD COLUMN login_code_expires_at DATETIME null;