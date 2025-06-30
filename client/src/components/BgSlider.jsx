import { act, useState } from 'react'
import { assets, categories } from '../assets/assets';

const BgSlider = () => {

    const [sliderPosition, setSliderPosition] = useState(50);
    const [activeCatgeory, setActiveCatgeory] = useState("People");

    const handleSliderChange = (e) => {
        setSliderPosition(e.target.value);
    }

  return (
    <div className='mb-16 relative'>
        {/* Section Title */}
        <h2 className="text-3xl md:text-4xl font-bold text-gray-900 mb-12 text-center">
            Stunning quality.
        </h2>

        {/* Category Title */}
        <div className="flex justify-center mb-10 flex-wrap">
            <div className="inline-flex gap-4 bg-gray-100 p-2 rounded-full flex-wrap justify-center">
                {categories.map((category) => (
                    <button key={category} className={`px-6 py-2 rounded-full font-medium ${activeCatgeory === category ? 'bg-white text-gray-800 shadow-sm' : 'text-gray-600 hover:bg-gray-200'} `}
                        onClick= {() => setActiveCatgeory(category)}>
                        {category}
                    </button>
                ))}
            </div>
        </div>

        {/*Image comparison slider */}

         {/* <div className="relative w-full max-w-4xl overflow-hidden m-auto rounded-xl shadow-lg">
            <img src={assets.people_org} alt="orignal image" style={{clipPath: `inset(0 ${100.2 - sliderPosition} % 0 0 )`}}/>

            <img src={assets.people} alt="removed background image"  style={{clipPath: `inset(0 0 0 ${sliderPosition} % )`}} className='absolute top-0 left-0 w-full h-full z-10'/>
        </div>  */}

        <div className="relative w-full max-w-4xl overflow-hidden m-auto rounded-xl shadow-lg">
        <img
             src={assets.people_org}
            alt="original image"
            style={{ clipPath: `inset(0 ${100.2 - sliderPosition}% 0 0)` }}
            className="w-full h-full"
        />

        <img
            src={assets.people}
            alt="removed background image"
            style={{ clipPath: `inset(0 0 0 ${sliderPosition}%)` }}
            className="absolute top-0 left-0 w-full h-full z-10"
        />

        <input type="range" 
          className='absolute top-1/2 left-1/2 -translate-x-1/2 -translate-y-1/2 w-full z-10 slider '
          min={0}
          max={100}
          onChange= {handleSliderChange}/>

</div> 

    </div>
  )
}

export default BgSlider
