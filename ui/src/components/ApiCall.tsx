import { useState } from 'react';
import { useAuth0 } from '@auth0/auth0-react';

export default function ApiCall() {
  const { getAccessTokenSilently } = useAuth0();
  const [apiResponse, setApiResponse] = useState(null);

  const callProtectedApi = async () => {
    try {
      const token = await getAccessTokenSilently();

      const response = await fetch('http://localhost:8080/api/card/1/detail', {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      const data = await response.json();
      setApiResponse(data);
    } catch (error) {
      console.error('API call failed:', error);
    }
  };

  return (
    <div>
      <button onClick={callProtectedApi}>Call API</button>
      {apiResponse && <pre>{JSON.stringify(apiResponse, null, 2)}</pre>}
    </div>
  );
}
