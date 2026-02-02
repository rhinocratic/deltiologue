import { useLoaderData } from "react-router-dom";
import { AboutLoaderResult } from "./aboutLoader";
import AboutSection from "../../components/about/AboutSection";

export default function AboutPage() {

  const { data } = useLoaderData() as AboutLoaderResult;

  const renderedSections = data.map(({ title, content }) => {
    return <AboutSection title={title} content={content} />;
  });

  return (
    <div>
      {renderedSections}
    </div>
  );
}