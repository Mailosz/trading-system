

export function findTickers(searchstring: string): Promise<string[]> {


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
                console.log('Tickers List:', data);
                return data;
            })
            .catch(error => {
                console.error('Error fetching tickers list:', error);
                return [];
            });

    }
