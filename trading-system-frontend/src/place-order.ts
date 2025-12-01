import { LitElement, html, css } from 'lit';
import { property, customElement } from 'lit/decorators.js';
import { findTickers } from './tickers-service';


@customElement('place-order')
export class PlaceOrderElement extends LitElement {


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
        <input type="text" placeholder="Enter ticker symbol" @input="${this.handleInput}" />
      </div>
    `;
  }

  handleInput(event: Event) {
    const input = event.target as HTMLInputElement;
    const value = input.value;

    if (value.length > 1) {
      findTickers(value).then(tickers => {
        console.log('Found tickers:', tickers);
      });
    }

  }
}
