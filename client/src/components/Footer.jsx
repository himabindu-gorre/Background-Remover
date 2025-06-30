import { assets, FOOTER_CONSTANTS } from "../assets/assets.js";

const Footer = () => {
  return (
    <footer className="flex flex-col item-center sm:flex-row justify-between gap-4 px-4 lg:px-44 py-3 text-center sm:text-left">
      <img src={assets.logo} alt="logo" width={32} />

      <p className="flex-1 border-l border-gray-100 max-sm:active">
        &copy; {
        new Date().getFullYear()} @Hima Bindu Gorre | All Rights
        Reversed.
      </p>

      <div className="flex gap-3 mt-2 sm:mt-0">
        {FOOTER_CONSTANTS.map((item, index) => (
          <a href={item.url} key={index} target="_blank" rel="noopener noreferrer">
            <img src={item.logo} alt="logo" width={32} />
          </a>
        ))}
      </div>
    </footer>
  );
};

export default Footer;
