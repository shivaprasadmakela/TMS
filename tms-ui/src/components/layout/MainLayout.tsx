import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';
import { Bell, Search } from 'lucide-react';
import { useSelector } from 'react-redux';
import { RootState } from '../../store';

const MainLayout = () => {
  const auth = useSelector((state: RootState) => state.auth);

  return (
    <div className="flex bg-[var(--bg-main)] min-h-screen">
      <Sidebar />
      <div className="flex-1 ml-64 flex flex-col">
        {/* Navbar */}
        <header className="h-16 bg-white border-b border-[var(--border)] px-8 flex items-center justify-between sticky top-0 z-10">
          <div className="relative w-96">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 text-[var(--text-muted)]" size={18} />
            <input 
              type="text" 
              placeholder="Search projects, tasks..." 
              className="pl-10 h-10 bg-slate-50 border-none hover:bg-slate-100 focus:bg-white transition-all"
            />
          </div>

          <div className="flex items-center gap-6">
            <button className="text-[var(--text-muted)] hover:text-[var(--primary)] transition-all">
              <Bell size={20} />
            </button>
            
            <div className="flex items-center gap-3 pl-6 border-l border-[var(--border)]">
              <div className="text-right hidden sm:block">
                <p className="text-sm font-semibold">{auth.user?.firstName} {auth.user?.lastName}</p>
                <p className="text-xs text-[var(--text-muted)] capitalize">{auth.user?.role}</p>
              </div>
              <div className="w-10 h-10 rounded-full bg-[var(--primary)] flex items-center justify-center text-white font-bold">
                {auth.user?.firstName?.[0]}{auth.user?.lastName?.[0]}
              </div>
            </div>
          </div>
        </header>

        {/* Content Area */}
        <main className="p-8 animate-fade-in flex-1 overflow-auto">
          <Outlet />
        </main>
      </div>
    </div>
  );
};

export default MainLayout;
