
CREATE TABLE IF NOT EXISTS public.orders (
    id uuid NOT NULL,
    account_id bigint NOT NULL,
    commision double precision,
    execution_price double precision,
    filled_date bigint,
    isin character varying(255) NOT NULL,
    order_id bigint NOT NULL,
    quantity integer,
    registration_time bigint,
    status smallint NOT NULL,
    CONSTRAINT orders_status_check CHECK (((status >= 0) AND (status <= 2)))
);

CREATE TABLE IF NOT EXISTS public.tickers (
    isin character varying(255) NOT NULL,
    mic character varying(255),
    name character varying(255),
    ticker character varying(255),
    trade_currency character varying(255)
);

ALTER TABLE ONLY public.orders
    DROP CONSTRAINT IF EXISTS fkdwlncenh8tlndumw9m04xielw;

ALTER TABLE ONLY public.orders
    DROP CONSTRAINT IF EXISTS orders_pkey;

ALTER TABLE ONLY public.tickers
    DROP CONSTRAINT IF EXISTS tickers_pkey;



ALTER TABLE ONLY public.tickers
    ADD CONSTRAINT tickers_pkey PRIMARY KEY (isin);

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT orders_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.orders
    ADD CONSTRAINT fkdwlncenh8tlndumw9m04xielw FOREIGN KEY (isin) REFERENCES public.tickers(isin);