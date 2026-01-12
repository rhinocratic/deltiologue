import ApiCall from "../../components/ApiCall";

export default function HomePage() {
  return (
    <div>
      <div>Home Page</div>
      <div>Domain: {import.meta.env.VITE_AUTH0_DOMAIN}</div>
      <div>Client ID: {import.meta.env.VITE_AUTH0_CLIENT_ID}</div>
      <ApiCall />
    </div>
  );
};
