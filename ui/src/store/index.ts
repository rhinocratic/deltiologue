import { configureStore } from "@reduxjs/toolkit";
import { setupListeners } from "@reduxjs/toolkit/query";
import { summariesApi } from "./apis/summariesApi";

export const store = configureStore({
    reducer: {
        [summariesApi.reducerPath]: summariesApi.reducer,
    },
    middleware: (getDefaultMiddleware) => {
        return getDefaultMiddleware()
            .concat(summariesApi.middleware);
    },
});

setupListeners(store.dispatch);

export {
    useFetchSummariesQuery
} from './apis/summariesApi';
