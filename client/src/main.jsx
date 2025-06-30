import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'
import './index.css'
import {BrowserRouter} from 'react-router-dom'
import { ClerkProvider } from '@clerk/clerk-react';
// import  { AppContextProvider }  from './Context/AppContext.jsx';
import { AppContextProvider } from './Context/AppContext.jsx';

const PUBLISABLE_KEY = import.meta.env.VITE_CLERK_PUBLISHABLE_KEY;

if(!PUBLISABLE_KEY) {
  throw new Error('Missing Publishable Key');
}

ReactDOM.createRoot(document.getElementById('root')).render(
    <BrowserRouter>
        <ClerkProvider publishableKey = {PUBLISABLE_KEY}>
          <AppContextProvider>
              <App />
            </AppContextProvider>
        </ClerkProvider>
    </BrowserRouter>
)
