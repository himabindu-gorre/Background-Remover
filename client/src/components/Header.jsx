import React, { useContext } from 'react';
import { assets } from '../assets/assets';
import { AppContext } from '../Context/AppContext';

const Header = () => {
  const { removeBg } = useContext(AppContext);

  return (
    <div className="grid grid-cols-1 md:grid-cols-2 gap-12 items-center mb-16">
      <div className="order-2 md:order-1 flex justify-center">
        <div className="shadow-[0_25px_50px_-12px_rgba(0,0,0,0.15)] rounded-3xl overflow-hidden">
          <video
            src={assets.video_banner}
            autoPlay
            loop
            muted
            className="w-full max-w-[400px] h-auto object-cover"
          />
        </div>
      </div>

      <div className="order-1 md:order-2">
        <h1 className="text-4xl md:text-5xl font-bold text-gray-900 mb-6 leading-tight">
          The fastest <span className='text-indigo-700'>background eraser.</span>
        </h1>
        <p className="text-gray-600 mb-8 text-lg leading-relaxed">
          Transform your photos with our background remover app! Highlight your subject and create a transparent background.
        </p>

        <div>
          <input
            type="file"
            accept="image/*"
            id="upload1"
            hidden
            onChange={(e) => removeBg(e.target.files[0])}
          />
          <label
            htmlFor="upload1"
            className="bg-black text-white font-medium px-8 py-4 rounded-full hover:opacity-90 transition-transform hover:scale-105 text-lg"
          >
            Upload your image
          </label>
        </div>
      </div>
    </div>
  );
};

export default Header;
