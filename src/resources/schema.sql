CREATE TABLE IF NOT EXISTS USUARIOS (
   ID BIGINT AUTO_INCREMENT PRIMARY KEY,
   USERNAME VARCHAR(30),
   PASSWORD VARCHAR(10),
   EMAIL VARCHAR(100),
   ISLOGGEDIN TINYINT(1)
);
INSERT INTO USUARIOS (
   USERNAME,
   PASSWORD,
   EMAIL,
   ISLOGGEDIN
) values ( 'luisangel',
           '12345',
           'luis@example.com',
           0 ),( 'luisangel2',
  '54321',
  'luis2@example.com',
  0 );