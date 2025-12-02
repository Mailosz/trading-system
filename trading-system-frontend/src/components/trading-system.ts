import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { Router } from '@lit-labs/router';

@customElement('trading-system')
export class TradingSystem extends LitElement {


  static styles = css`
    :host {
      min-height: 100vh;
      display: flex;
      flex-direction: column;
      align-items: stretch;
      justify-content: flex-start;
      color: #1a2b42;
      max-width: 600px;
      margin: 0 auto;
      text-align: center;
      background-color: var(--trading-system-background-color);
      font-size: 12px;
      padding: 8px;
    }

    main {
      flex-grow: 1;
    }

    .logo {
      margin-top: 36px;
      animation: app-logo-spin infinite 20s linear;
    }

    @keyframes app-logo-spin {
      from {
        transform: rotate(0deg);
      }
      to {
        transform: rotate(360deg);
      }
    }

    .app-footer {
      font-size: calc(12px + 0.5vmin);
      align-items: center;
    }

    .app-footer a {
      margin-left: 5px;
    }
  `;

    private router = new Router(this, [
      { path: '/order', render: () => html`<place-order></place-order>` },
      { path: '/orders/*', render: () => html`<order-details></order-details>` },
      { path: '/**', render: () => html`<order-list></order-list>` },
    ]);

  render() {
    return html`
      <main>
        ${this.router.outlet()}
      </main>

      <p class="app-footer">
        by Miłosz Osadczuk
      </p>
    `;
  }
}