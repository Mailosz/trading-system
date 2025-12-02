import { Ticker } from "./types";


export function findTickers(searchstring: string): Promise<Ticker[]> {


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
