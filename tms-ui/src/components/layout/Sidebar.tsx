import { NavLink } from 'react-router-dom';
import { 
  Briefcase, 
  CheckSquare, 
  Users, 
  Settings, 
  LogOut,
  LayoutDashboard
} from 'lucide-react';
import { useDispatch } from 'react-redux';
import { logout } from '../../store/slices/authSlice';
import { AppDispatch } from '../../store';

const Sidebar = () => {
  const dispatch = useDispatch<AppDispatch>();

  const navItems = [
    { icon: LayoutDashboard, label: 'Dashboard', path: '/dashboard' },
    { icon: Briefcase, label: 'Projects', path: '/projects' },
    { icon: CheckSquare, label: 'My Tasks', path: '/tasks' },
    { icon: Users, label: 'Team', path: '/team' },
    { icon: Settings, label: 'Settings', path: '/settings' },
  ];

  return (
    <div className="w-64 h-screen bg-[var(--bg-sidebar)] border-r border-[var(--border)] flex flex-col fixed left-0 top-0">
      <div className="p-6">
        <h1 className="text-2xl font-bold text-[var(--primary)] flex items-center gap-2">
          <CheckSquare size={28} />
          TMS Pro
        </h1>
      </div>

      <nav className="flex-1 px-4 py-4 space-y-1">
        {navItems.map((item) => (
          <NavLink
            key={item.path}
            to={item.path}
            className={({ isActive }: { isActive: boolean }) => `
              flex items-center gap-3 px-4 py-3 rounded-[var(--radius)] font-medium transition-all
              ${isActive 
                ? 'bg-[var(--primary)] text-white shadow-md' 
                : 'text-[var(--text-muted)] hover:bg-slate-50 hover:text-[var(--text-main)]'}
            `}
          >
            <item.icon size={20} />
            {item.label}
          </NavLink>
        ))}
      </nav>

      <div className="p-4 border-t border-[var(--border)]">
        <button 
          onClick={() => dispatch(logout())}
          className="flex items-center gap-3 px-4 py-3 w-full text-[var(--text-muted)] hover:text-red-600 hover:bg-red-50 rounded-[var(--radius)] transition-all"
        >
          <LogOut size={20} />
          Sign Out
        </button>
      </div>
    </div>
  );
};

export default Sidebar;
