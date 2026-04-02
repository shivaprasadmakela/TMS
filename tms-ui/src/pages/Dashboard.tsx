import { useSelector } from "react-redux";
import { RootState } from "../store";
import { 
  Briefcase, 
  CheckSquare, 
  Clock, 
  AlertCircle,
  TrendingUp
} from "lucide-react";

const Dashboard = () => {
  const auth = useSelector((state: RootState) => state.auth);

  const stats = [
    { label: 'Total Projects', value: '12', icon: Briefcase, color: 'text-blue-600', bg: 'bg-blue-50' },
    { label: 'Active Tasks', value: '48', icon: CheckSquare, color: 'text-indigo-600', bg: 'bg-indigo-50' },
    { label: 'In Progress', value: '16', icon: Clock, color: 'text-amber-600', bg: 'bg-amber-50' },
    { label: 'Overdue', value: '3', icon: AlertCircle, color: 'text-red-600', bg: 'bg-red-50' },
  ];

  const recentActivity = [
    { id: 1, user: 'John Doe', action: 'completed task', target: 'Design System Update', time: '2h ago' },
    { id: 2, user: 'Jane Smith', action: 'created project', target: 'Marketing Campaign', time: '4h ago' },
    { id: 3, user: 'Mike Ross', action: 'added comment to', target: 'API Integration', time: '5h ago' },
  ];

  return (
    <div className="space-y-8">
      <div>
        <h2 className="text-3xl font-bold text-[var(--text-main)]">Welcome back, {auth.user?.firstName}!</h2>
        <p className="text-[var(--text-muted)] mt-1">Here's what's happening with your projects today.</p>
      </div>

      {/* Stats Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        {stats.map((stat, i) => (
          <div key={i} className="bg-white p-6 rounded-[var(--radius)] border border-[var(--border)] shadow-sm hover:shadow-md transition-all">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-[var(--text-muted)]">{stat.label}</p>
                <h3 className="text-2xl font-bold mt-1">{stat.value}</h3>
              </div>
              <div className={`${stat.bg} ${stat.color} p-3 rounded-lg`}>
                <stat.icon size={24} />
              </div>
            </div>
            <div className="mt-4 flex items-center gap-1 text-xs font-medium text-emerald-600">
              <TrendingUp size={14} />
              <span>+12% from last week</span>
            </div>
          </div>
        ))}
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Recent Activity */}
        <div className="lg:col-span-2 bg-white rounded-[var(--radius)] border border-[var(--border)] shadow-sm">
          <div className="p-6 border-b border-[var(--border)] flex items-center justify-between">
            <h3 className="font-bold text-lg">Recent Activity</h3>
            <button className="text-[var(--primary)] text-sm font-semibold hover:underline">View All</button>
          </div>
          <div className="p-6">
            <div className="space-y-6">
              {recentActivity.map((activity) => (
                <div key={activity.id} className="flex items-start gap-4">
                  <div className="w-10 h-10 rounded-full bg-slate-100 flex items-center justify-center font-bold text-slate-500 text-sm">
                    {activity.user.split(' ').map(n => n[0]).join('')}
                  </div>
                  <div className="flex-1">
                    <p className="text-sm">
                      <span className="font-bold">{activity.user}</span>
                      <span className="text-[var(--text-muted)] mx-1">{activity.action}</span>
                      <span className="font-semibold text-[var(--primary)]">{activity.target}</span>
                    </p>
                    <p className="text-xs text-[var(--text-muted)] mt-1">{activity.time}</p>
                  </div>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* Quick Actions */}
        <div className="bg-white rounded-[var(--radius)] border border-[var(--border)] shadow-sm p-6">
          <h3 className="font-bold text-lg mb-6">Quick Actions</h3>
          <div className="space-y-4">
            <button className="w-full h-12 bg-[var(--primary)] text-white font-bold rounded-[var(--radius)] shadow-sm hover:bg-[var(--primary-hover)] transition-all flex items-center justify-center gap-2">
              <Briefcase size={20} />
              New Project
            </button>
            <button className="w-full h-12 bg-white text-[var(--primary)] border border-[var(--primary)] font-bold rounded-[var(--radius)] hover:bg-slate-50 transition-all flex items-center justify-center gap-2">
              <CheckSquare size={20} />
              Create Task
            </button>
          </div>
          
          <div className="mt-8">
            <h4 className="text-sm font-bold text-[var(--text-muted)] uppercase tracking-wider mb-4">Performance</h4>
            <div className="space-y-4">
              <div>
                <div className="flex justify-between text-xs mb-1">
                  <span>Task Completion</span>
                  <span>78%</span>
                </div>
                <div className="w-full h-2 bg-slate-100 rounded-full overflow-hidden">
                  <div className="h-full bg-emerald-500" style={{ width: '78%' }}></div>
                </div>
              </div>
              <div>
                <div className="flex justify-between text-xs mb-1">
                  <span>Project Progress</span>
                  <span>45%</span>
                </div>
                <div className="w-full h-2 bg-slate-100 rounded-full overflow-hidden">
                  <div className="h-full bg-blue-500" style={{ width: '45%' }}></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Dashboard;
