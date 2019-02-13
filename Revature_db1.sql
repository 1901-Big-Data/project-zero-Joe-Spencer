CREATE SEQUENCE ACCOUNTS_ID_seq
    start with 1
    increment by 1;
    
CREATE OR REPLACE TRIGGER ACCOUNTS_trigger_id 
BEFORE INSERT ON ACCOUNTS
FOR EACH ROW
BEGIN 
    IF :new.ACCOUNT_ID IS NULL THEN
    SELECT ACCOUNTS_ID_seq.nextval INTO :new.ACCOUNT_ID from dual;
    END IF;
END;    

/




CREATE SEQUENCE USERS_ID_seq
    start with 1
    increment by 1;
    
CREATE OR REPLACE TRIGGER USERS_trigger_id 
BEFORE INSERT ON USERS
FOR EACH ROW
BEGIN 
    IF :new.ID IS NULL THEN
    SELECT USERS_ID_seq.nextval INTO :new.ID from dual;
    END IF;
END;    

/
create or replace procedure attempt_login(users_username in varchar2, ppassword in varchar2, pass out number, id out number) as
    users_password varchar2(200);
    userId number(10);
begin
    select password, ID into users_password, userId from users where username = users_username;
    if users_password = ppassword then
        pass := 1;
        id := userId;
    else
        pass := 0;
        id := 0;
    end if;
end;
