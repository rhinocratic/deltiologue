import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Root from "./pages/Root";
import HomePage from "./pages/home/HomePage";
import DetailsPage from "./pages/details/DetailsPage";
import SearchPage from "./pages/search/SearchPage";
import NotesPage from "./pages/notes/NotesPage";
import NotFoundPage from "./pages/error/NotFoundPage";
import { searchLoader } from "./pages/search/searchLoader";
import { detailsLoader } from "./pages/details/detailsLoader";
import { noteCatalogueLoader } from "./pages/notes/noteCatalogueLoader";
import "primereact/resources/themes/md-light-deeppurple/theme.css";
import AboutPage from "./pages/about/AboutPage";
import { aboutLoader } from "./pages/about/aboutLoader";
import CataloguePage from "./pages/catalogue/CataloguePage";
import { catalogueLoader } from "./pages/catalogue/catalogueLoader";

const router = createBrowserRouter([
  {
    path: "/",
    element: <Root />,
    children: [
      {
        index: true,
        path: "/",
        element: <HomePage />,
      },
      {
        path: "/catalogue",
        element: <CataloguePage />,
        loader: catalogueLoader
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
        path: "/about",
        element: <AboutPage />,
        loader: aboutLoader
      },
      {
        path: "*",
        element: <NotFoundPage />
      }
    ],
  },
]);

function App() {
  return (
    <RouterProvider router={router} />
  );
}

export default App;
