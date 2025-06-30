import Footer from './components/Footer.jsx'
import Menubar from './components/Menubar.jsx'
import Home from './Pages/Home.jsx'
import { Routes , Route} from 'react-router-dom'
import {Toaster} from 'react-hot-toast'
import UserSyncHandler from './components/UserSyncHandler.jsx'
import { RedirectToSignIn, SignedIn, SignedOut } from '@clerk/clerk-react'
import Result from './Pages/Result.jsx';
// import { RedirectToSignIn } from '@clerk/clerk-react'

const App = () => {
  return (
    <div>
      <UserSyncHandler />
      <Menubar />
      <Toaster />
      <Routes>
          <Route path='/' element={<Home />}></Route>
          <Route path='/result' element={
            <>
                <SignedIn>
                  <Result />
                </SignedIn>
                <SignedOut>
                  <RedirectToSignIn />
                </SignedOut>
            </>
          }
          />
      </Routes>
      <Footer />
    </div>
  )
}

export default App
