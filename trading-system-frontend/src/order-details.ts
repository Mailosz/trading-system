import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { OrderDetails } from './types';
import { getStatusLabel } from './utils';


@customElement('order-details')
export class OrderDetailsElement extends LitElement {

    @property({ type: Object })
    orderData : OrderDetails | null | undefined = undefined;


  static styles = css`
    :host {

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



    #order-grid {
      display: grid;
      grid-template-columns: auto 1fr;
      margin: 6px;
      background-color: #f4f4f4;
      padding: 12px 20px;
      border-radius: 6px;
      gap: 12px 20px;
      box-shadow: 0px 2px 6px rgba(0, 0, 0, 0.1);
    }

    .label {
      text-align: right;
      align-self: end;
    }

    .value {
      text-align: left;
      font-size: 1.2em;
    }
  `;

  async firstUpdated() {

    const id = location.pathname.split('/').pop();

    if (id) {
      try {
        const response = await fetch(`/api/orders/${id}`);
        this.orderData = await response.json();
      } catch (err) {
        this.orderData = null;
        console.error('Fetch failed:', err);
      }
    }


  }

  render() {
    return html`
      
      <a href="/" class="button" style="margin: 12px 0;">Powrót do listy zleceń</a>
      <div style="display: flex; flex-direction: column; align-items: center;">
        <div id="order-grid">
          <div style="grid-column: span 2; font-size: 1.4em; color: #555;">Szczegóły zlecenia</div>

          ${this.orderData === undefined ? html`<div>Ładowanie danych zlecenia...</div>`: 
            this.orderData === null ? html`<div>Brak zlecenia</div>`  :
              html`
              <div class="label">Numer zlecenia:</div>
              <div class="value">${this.orderData.orderId}</div>

              <div class="label">Status zlecenia:</div>
              <div class="value">${getStatusLabel(this.orderData.status)}</div>

              <div class="label">ISIN:</div>
              <div class="value">${this.orderData.isin}</div>

              <div class="label">Instrument:</div>
              <div class="value">${this.orderData.tickerName}</div>

              <div class="label">Waluta:</div>
              <div class="value">${this.orderData.currency}</div>

              <div class="label">Kurs akcji:</div>
              <div class="value">${this.orderData.priceLimit}</div>

              <div class="label">Liczba:</div>
              <div class="value">${this.orderData.quantity}</div>

              <div class="label">Wartość zlecenia:</div>
              <div class="value">${this.orderData.quantity * this.orderData.priceLimit}</div>

              <div class="label">Data utworzenia:</div>
              <div class="value">${new Date(this.orderData.registrationTime * 1000).toLocaleString()}</div>

              <div class="label">Data wykonania:</div>
              <div class="value">${this.orderData.filledDate ? new Date(this.orderData.filledDate * 1000).toLocaleString() : html`<i>-- brak daty --</i>`}</div>

              <div class="label">Prowizja:</div>
              <div class="value">${this.orderData.commission}</div>
            `}

        </div>
      </div>
    `;
  }
}


