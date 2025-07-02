import { useSearchParams } from 'react-router-dom';
import { useEffect } from 'react';

function Home() {

  const [searchParams] = useSearchParams();

  useEffect(() => {
    const status = searchParams.get('verified');
    if (status === 'true') {
      alert("Email verified successfully! You can now log in.");
    } else if (status === 'false') {
      alert("Invalid or expired verification link.");
    }
  }, [searchParams]);

  return (
    <>
    </>
  );
}

export default Home;
