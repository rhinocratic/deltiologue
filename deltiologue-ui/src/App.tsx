import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Root from "./pages/Root";
import HomePage from "./pages/home/HomePage";
import DetailsPage from "./pages/details/DetailsPage";
import SearchPage from "./pages/search/SearchPage";
import LinksPage from "./pages/links/LinksPage";
import NotesPage from "./pages/notes/NotesPage";
import { searchLoader } from "./pages/search/searchLoader";
import { detailsLoader } from "./pages/details/detailsLoader";
import { noteCatalogueLoader } from "./pages/notes/noteCatalogueLoader";
import { PrimeReactProvider } from 'primereact/api';


const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        index: true,
        element: <HomePage />,
      },
      {
        path: "/details/:index",
        element: <DetailsPage />,
        loader: detailsLoader
      },
      {
        path: "/search",
        element: <SearchPage />,
        loader: searchLoader
      },
      {
        path: "/notes",
        element: <NotesPage />,
        loader: noteCatalogueLoader
      },
      {
        path: "/links",
        element: <LinksPage />,
      },
    ],
  },
]);

function App() {
  return (
    <PrimeReactProvider>
      <RouterProvider router={router} />
    </PrimeReactProvider>
  );
}

export default App;
