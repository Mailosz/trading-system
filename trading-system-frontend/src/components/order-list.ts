import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { Order, OrderStatus } from '../types';
import {getStatusLabel} from '../utils';


@customElement('order-list')
export class OrderListElement extends LitElement {

    @property({ type: Array })
    listData : Order[] = [];

  static styles = css`
    :host {
      font-size: 12px;
    }

    #new-order {
      margin:  4px 0 12px 0;
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

    #orders-list {
      display: flex;
      flex-direction: column;
      gap: 4px;
    }

    .first-row {
      position: sticky;
      top: 0;
      margin: 6px 0;
    }

    .first-row>div {
      background-color: rgba(0, 0, 0, 0.05);
      padding: 8px 0;
      border-radius: 6px;
    }

    .row, .first-row {
      margin: 2px 0;
      display: grid;
      grid-template-columns: 2fr 2fr 2fr 1fr 1fr 2fr;
      gap: 4px;
    }

    .row {
      background-color: rgba(255, 255, 255, 0.3);
      border-radius: 6px;
      padding: 8px 4px;;
    }

    .row:hover {
      background-color: rgba(255, 255, 255, 0.6);
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
    <a href="/order" id="new-order" class="button">Złóż nowe zlecenie</a>
      <div id="orders-list">
          <div class="first-row">
            <div>Numer zlecenia</div>
            <div>Status</div>
            <div>ISIN</div>
            <div>Liczba</div>
            <div>Cena</div>
            <div>Szczegóły</div>
          </div>
          ${this.listData.map(
              (entry, i) => html`
                <div class="row">
                  <div>${entry.orderId}</div>
                  <div>${getStatusLabel(entry.status)}</div>
                  <div>${entry.isin}</div>
                  <div>${entry.quantity}</div>
                  <div>${entry.executionPrice}</div>
                  <div>${entry.status === 'FILLED' ? html`<a href="/orders/${entry.id}">Zobacz szczegóły</a>` : html``}</div>
                </div>
              `,
            )}
        ${this.listData.length === 0 ? html`<div style="text-align: center;">Brak zleceń do wyświetlenia.</div>` : html``}
      </div>
    `;
  }
}


