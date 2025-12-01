import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';


@customElement('order-list')
export class OrderListElement extends LitElement {


  static styles = css`
    :host {
      border: 2px solid red;
      width: 100px;
      height: 100px;
    }

  `;

  render() {
    return html`
      <div>
        Test
      </div>
    `;
  }
}
