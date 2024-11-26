CREATE TABLE NOTIFICATIONS (
    NOTIFICATION_ID UUID NOT NULL DEFAULT gen_random_uuid(),
    USER_ID UUID NOT NULL,
    TITLE VARCHAR(150) NOT NULL,
    MESSAGE VARCHAR(250) NOT NULL,
    NOTIFICATION_STATUS VARCHAR(10) NOT NULL,
    CREATION_DATE timestamp NOT NULL,
    PRIMARY KEY (NOTIFICATION_ID)
);