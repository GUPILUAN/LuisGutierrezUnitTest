

CREATE TABLE USUARIOS (
   ID BIGINT AUTO_INCREMENT PRIMARY KEY,
   USERNAME VARCHAR(30),
   PASSWORD VARCHAR(10),
   EMAIL VARCHAR(100),
   ISLOGGEDIN TINYINT(1)
);
