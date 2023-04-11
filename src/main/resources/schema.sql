CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    password VARCHAR(255) NOT NULL,
    role VARCHAR(10) NOT NULL DEFAULT 'USER',
    status VARCHAR(10) NOT NULL,
    activation_code VARCHAR(255),
    currency VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS categories_income (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT unique_category_income_user UNIQUE (name, user_id)
);

CREATE TABLE IF NOT EXISTS categories_expense (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(100) NOT NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT unique_category_expense_user UNIQUE (name, user_id)
);

CREATE TABLE IF NOT EXISTS accounts (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(30) NOT NULL,
    start_amount DOUBLE PRECISION NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    currency VARCHAR(20) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT unique_name_user UNIQUE (name, user_id)
);

CREATE TABLE IF NOT EXISTS operations_income (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    description VARCHAR(500) NOT NULL,
    category_id BIGINT REFERENCES categories_income(id) ON DELETE CASCADE NOT NULL,
    account_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS operations_expense (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    description VARCHAR(500) NOT NULL,
    category_id BIGINT REFERENCES categories_expense(id) ON DELETE CASCADE NOT NULL,
    account_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL
);

CREATE TABLE IF NOT EXISTS plans_income (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    category_id BIGINT REFERENCES categories_income(id) NOT NULL,
    date TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT unique_plan_income UNIQUE (category_id, date, user_id)
);

CREATE TABLE IF NOT EXISTS plans_expense (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    category_id BIGINT REFERENCES categories_expense(id) NOT NULL,
    date TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL,
    CONSTRAINT unique_plan_expense UNIQUE (category_id, date, user_id)
);

CREATE TABLE IF NOT EXISTS operations_move (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY NOT NULL,
    account_from_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE NOT NULL,
    account_to_id BIGINT REFERENCES accounts(id) ON DELETE CASCADE NOT NULL,
    amount DOUBLE PRECISION NOT NULL,
    description VARCHAR(500) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE NOT NULL
);