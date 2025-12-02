import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { Order, OrderStatus } from './types';


@customElement('order-details')
export class OrderDetailsElement extends LitElement {

    @property({ type: Array })
    listData : Order[] = [];

  static styles = css`
    :host {
      font-size: 12px;
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
    }

    .value {
      text-align: left;
    }
  `;

  async firstUpdated() {
    try {
      const response = await fetch('/api/orders');
      this.listData = await response.json();
    } catch (err) {
      console.error('Fetch failed:', err);
    }
  }

  render() {
    return html`
      
        <a href="/" class="button" style="margin: 12px 0;">Powrót do listy zleceń</a>
      <div style="display: flex; flex-direction: column; align-items: center;">
        <div id="order-grid">
          <div style="grid-column: span 2; font-size: 20px; color: #555;">Szczegóły zlecenia</div>

          <div class="label">Numer zlecenia:</div>
          <div class="value">123456</div>

          <div class="label">Status zlecenia:</div>
          <div class="value">Zrealizowane</div>

          <div class="label">ISIN:</div>
          <div class="value">ISIN: US1234567890</div>

          <div class="label">Instrument:</div>
          <div class="value">ABC Corp</div>

          <div class="label">Waluta:</div>
          <div class="value">PLN</div>

          <div class="label">Kurs akcji:</div>
          <div class="value">50.00</div>

          <div class="label">Liczba:</div>
          <div class="value">100</div>

          <div class="label">Wartość zlecenia:</div>
          <div class="value">49.75</div>

          <div class="label">Data utworzenia:</div>
          <div class="value">2024-06-15 10:30:00</div>

          <div class="label">Data wykonania:</div>
          <div class="value">2024-06-15 10:45:00</div>

          <div class="label">Prowizja:</div>
          <div class="value">1.25</div>
        </div>
      </div>
    `;
  }
}


