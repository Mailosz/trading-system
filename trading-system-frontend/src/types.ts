export type Ticker = {
  isin: string;
  name: string;
  mic: string;
  price: string;
  tradeCurrency: string;
};


export type Order = {
  id: string;
  orderId: string;
  status: OrderStatus;
  isin : string;
  quantity: number;
  priceLimit: number;
}

export type OrderDetails = {
  id: string;
    orderId: number;
    status: OrderStatus;
    isin: string;
    tickerName: string;
    currency: string;
    executionPrice: number;
    quantity: number;
    registrationTime: number;
    filledDate: number;
    commission: number;
}

export type OrderType = 'LMT' | 'PKC' | 'PCR';

export type OrderStatus = "SUBMITTED" | "FILLED" | "EXPIRED";