import { useState } from "react";

export default function RemoteImage({ src, alt, className }) {
  const [failed, setFailed] = useState(false);

  if (failed) {
    return <div className={`${className || ""} image-fallback`} role="img" aria-label={alt} />;
  }

  return (
    <img
      className={className}
      src={src}
      alt={alt}
      onError={() => setFailed(true)}
    />
  );
}
