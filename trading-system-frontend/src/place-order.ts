import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { findTickers } from './tickers-service';
import { LionListbox, LionOption } from '@lion/ui/listbox.js';
import { Ticker } from './types';


@customElement('place-order')
export class PlaceOrderElement extends LitElement {

  @property({ type: Array })
  listBoxData : Ticker[] = [];

  @property({ type: Number })
  quantity: number = 0;

  @property({ type: String })
  orderType: string = 'LMT';

  @property({ type: Number })
  priceLimit: number = 0.00;

  @property({ type: Object })
  selectedTicker: Ticker | null = null;


  static styles = css`
    :host {

    }

    label {
      font-size: 12px;
      text-align: left;
      margin-top: 8px;
    }

    .button {
      display: inline-block;
      font-weight: bold;
      text-decoration: none;
      padding: 8px 16px;
      background-color: #ddd;
      border-radius: 6px;
      color: black;
      box-shadow: 0px 2px 6px rgba(0, 0, 0, 0.2);
    }
    .button:hover {
      filter: brightness(1.03);
    }
    .button:active {
      filter: brightness(0.9);
    }
  `;


  render() {
    return html`
    <a href="/" class="button" style="margin: 12px 0;">Powrót do listy zleceń</a>
      <div style="display: flex; flex-direction: column; gap: 4px;">

        <input type="text" placeholder="Enter ticker symbol" @input="${this.handleInput}" />
        <select multiple @input="${this.handleTickerSelected}">
          ${this.listBoxData.map(
            (entry, i) => html`
              <option .value="${entry.isin}">${entry.name}</option>
            `,
          )}
        </select>
        <label>Quantity:</label>
        <input type="number" placeholder="Quantity" .value="${this.quantity}" @input="${this.handleQuantityInput}" />
        <label>Order Type:</label>
        <select .value="${this.orderType}" @input="${this.handleOrderTypeChange}">
          <option value="LMT">LMT</option>
          <option value="PKC">PKC</option>
          <option value="PCR">PCR</option>
        </select>
        <label>Price limit:</label>
        <input type="number" placeholder="Price limit" .value="${this.priceLimit}" @input="${this.handlePriceLimitInput}" />
        <label>Valid until:</label>
        <input type="date" placeholder="Valid until" />
        <label>Current price:</label>
        <input type="text" placeholder="Current price" disabled .value="${this.selectedTicker?.price || '0.00'}" />
        <button @click="${this.placeOrder}">Place Order</button>
      </div>
    `;
  }

  handleInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if (value.length > 1) {
      findTickers(value).then(tickers => {
        this.listBoxData = tickers;
      });
    }

  }

  handleTickerSelected(event: Event) {
    const select = event.target as HTMLSelectElement;
    const selectedOption = select.selectedOptions.item(0);
    
    if (selectedOption) {
      this.selectedTicker = this.listBoxData.find(ticker => ticker.isin === selectedOption.value) || null;
    }
  }

  handleQuantityInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = parseInt(input.value, 10);
    this.quantity = isNaN(value) ? 0 : value;
  }

  handleOrderTypeChange(event: Event) {
    const select = event.target as HTMLSelectElement;
    this.orderType = select.value;
  }

  handlePriceLimitInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = parseFloat(input.value);
    this.priceLimit = isNaN(value) ? 0.00 : value;
  }

  placeOrder() {

    if (!this.selectedTicker) {
      return;
    }

    fetch('/api/orders/place', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        quantity: this.quantity,
        orderType: this.orderType,
        priceLimit: this.priceLimit,
        isin: this.selectedTicker?.isin || '',
        expiresAt: null
      }),
    }).then(response => {
      if (response.ok) {
        location.assign('/');
      } else {
        alert('Failed to place order.');
      }
    });


  }
}
