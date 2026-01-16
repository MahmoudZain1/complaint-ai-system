
INSERT INTO users (id, name, email, password, role, is_active)
VALUES (
           2,
           'Manager User',
           'manager@example.com',
           '$argon2id$v=19$m=16,t=2,p=1$NFZDbzlSN3cyYzFvOUdDcg$ODxHa3tniF8ovipPQPfDvWRIPfBCWGLuEiqkONExhy8',
           'MANAGER',
           true
       );
