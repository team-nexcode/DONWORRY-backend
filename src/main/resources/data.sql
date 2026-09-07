INSERT INTO users (user_id, name, avg_transfer_amount)
VALUES ('user_70s_01', '김순자', 650000);

INSERT INTO account_limit_logs (user_id, changed_at, is_increased)
VALUES ('user_70s_01', CURRENT_TIMESTAMP(), true);

INSERT INTO transfer_histories (user_id, recipient_account, recipient_name, amount, created_at)
VALUES ('user_70s_01', '999-888-777777', '김민수', 300000, CURRENT_TIMESTAMP());