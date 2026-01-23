import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { PostcardSummary } from '../types/postcardSummary';

export const summariesApi = createApi({
    reducerPath: 'summaries',
    baseQuery: fetchBaseQuery({ baseUrl: 'http://localhost:3005' }),
    endpoints(build) {
        return {
            fetchSummaries: build.query<PostcardSummary[], string>({
                query: (term) => `/summaries/search?term=${term}`
            })
        };
    }
});

export const { useFetchSummariesQuery } = summariesApi;