import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { Ticker } from '../types';


@customElement('place-order')
export class PlaceOrderElement extends LitElement {

  @property({ type: Array })
  listBoxData : Ticker[] = [];

  @property({ type: Number })
  quantity: number | null= null;

  @property({ type: String })
  orderType: string = 'LMT';

  @property({ type: Number })
  priceLimit: number | null = null;

  @property({ type: Object })
  selectedTicker: Ticker | null = null;

  @property({ type: Date })
  expiresAt: Date | null = null;

  @property({ type: Number })
  currentPrice: number | null = null;


  static styles = css`
    :host {

    }

    #order-form {
      display: flex; 
      flex-direction: column; 
      gap: 4px;
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
      border: none;
      cursor: pointer;
    }
    .button:hover {
      filter: brightness(1.03);
    }
    .button:active {
      filter: brightness(0.9);
    }

    input, select {
      padding: 6px 4px;
      border-radius: 6px;
      border: 1px solid #aaa;
    }

    select {
      padding: 6px 0px;
      overflow-y: auto;
    }
  `;


  render() {
    return html`
    <a href="/" class="button" style="margin: 4px 0 12px 0;">Powrót do listy zleceń</a>
      <form id="order-form">

        <input type="text" placeholder="Wyszukaj instrument..." @input="${this.handleInput}" autofocus/>
        <select multiple @input="${this.handleTickerSelected}" required>
          ${this.listBoxData.map(
            (entry, i) => html`
              <option .value="${entry.isin}">${entry.ticker} (${entry.isin}, ${entry.name})</option>
            `,
          )}
        </select>
        <label>Liczba do zakupu:</label>
        <input type="number" placeholder="Podaj ilość" .value="${this.quantity}" @input="${this.handleQuantityInput}" required min="1" />
        <label>Rodzaj zlecenia:</label>
        <select .value="${this.orderType}" @input="${this.handleOrderTypeChange}">
          <option value="LMT">LMT - zlecenie z limitem</option>
          <option value="PKC">PKC - po każdej cenie</option>
          <option value="PCR">PCR - po cenie rynkowej</option>
        </select>
        ${this.orderType === 'LMT' ? html`
          <label>Limit ceny:</label>
          <input type="number" placeholder="Podaj limit ceny" .value="${this.priceLimit}" @input="${this.handlePriceLimitInput}" required min="0" step="0.01" />
          ` : html``}
        <label>Zlecenie ważne do (pole opcjonalne):</label>
        <input type="datetime-local" @input="${this.handleExpiresAtInput}" />
        <label>Current price:</label>
        <input type="text" placeholder="Current price" disabled .value="${this.currentPrice || ''}" />
        <button type="button" class="button" @click="${this.placeOrder}" style="margin: 12px 0;">Złóż zlecenie</button>
      </form>
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
  

      const ticker = this.listBoxData.find(ticker => ticker.isin === selectedOption.value) || null;

      if (ticker !== this.selectedTicker) {
        this.selectedTicker = ticker;

        if (ticker) {
          fetch(`/api/tickers/price/${ticker.isin}`).then(response => response.json()).then(data => {
            console.log('Current price data:', data);
            this.currentPrice = Number.parseFloat(data.price);
          }).catch(error => {
            console.error('Error fetching current price:', error);
            this.currentPrice = null;
          });
        } else {
          this.currentPrice = null;
        }
      }
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

  handleExpiresAtInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value ? new Date(input.value) : null;
    this.expiresAt = value;
  }

  placeOrder() {

    if (!this.selectedTicker) {
      return;
    }

    const form = this.shadowRoot?.getElementById('order-form') as HTMLFormElement;
    if (!form.checkValidity()) {
      form.reportValidity();
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
        expiresAt: this.expiresAt ? Math.floor(this.expiresAt.getTime() / 1000) : null,
      }),
    }).then(response => {
      if (response.ok) {
        location.assign('/');
      } else {
        alert('Failed to place order.');
      }
    }).catch(error => {
      console.error('Error placing order:', error);
      alert('Error placing order.');
    });


  }
}


function findTickers(searchstring: string): Promise<Ticker[]> {


        let request = new Request(`/api/tickers/search`, {
            method: 'POST',
            headers: new Headers({
                'Content-Type': 'text/plain'
            }),
            body: searchstring
        });

        return fetch(request)
            .then(response => response.json())
            .then(data => {
                console.log(data)
                console.log('Tickers List:', data);
                return data;
            })
            .catch(error => {
                console.error('Error fetching tickers list:', error);
                return [];
            });

    }