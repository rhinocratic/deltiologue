interface AboutSectionProps {
  title: string;
  content: string;
}

export default function AboutSection({ title, content }: AboutSectionProps) {
  return (
    <div>
      <div>{title}</div>
      <div>{content}</div>
    </div>
  );
}