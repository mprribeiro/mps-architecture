CREATE TABLE COURSES (
    COURSE_ID UUID NOT NULL DEFAULT gen_random_uuid(),
    NAME VARCHAR(150) NOT NULL,
    DESCRIPTION VARCHAR(250) NOT NULL,
    COURSE_STATUS VARCHAR(10) NOT NULL,
    COURSE_LEVEL VARCHAR(15) NOT NULL,
    INSTRUCTOR_ID UUID  NOT NULL,
    IMAGE_URL VARCHAR(255),
    CREATION_DATE timestamp NOT NULL,
    UPDATE_DATE timestamp NOT NULL,
    PRIMARY KEY (COURSE_ID)
);