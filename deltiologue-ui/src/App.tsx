import { createBrowserRouter, RouterProvider } from "react-router-dom";
import Root from "./pages/Root";
import HomePage from "./pages/home/HomePage";
import DetailPage from "./pages/detail/DetailPage";
import SearchResultsPage from "./pages/search-results/SearchResultsPage";
import LinksPage from "./pages/links/LinksPage";
import NotesPage from "./pages/notes/NotesPage";

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
        path: "/detail",
        element: <DetailPage />,
      },
      {
        path: "/results",
        element: <SearchResultsPage />,
      },
      {
        path: "/notes",
        element: <NotesPage />,
      },
      {
        path: "/links",
        element: <LinksPage />,
      },
    ],
  },
]);

function App() {
  return <RouterProvider router={router} />;
}

export default App;
