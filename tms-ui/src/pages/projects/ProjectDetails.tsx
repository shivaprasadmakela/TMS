import { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../../store';
import { fetchTasksByProject } from '../../store/slices/taskSlice';
import { 
  Plus, 
  MoreVertical, 
  MessageSquare, 
  CheckSquare,
  ChevronLeft,
  Briefcase,
  Filter
} from 'lucide-react';

const ProjectDetails = () => {
  const { id } = useParams<{ id: string }>();
  const dispatch = useDispatch<AppDispatch>();
  const { tasks } = useSelector((state: RootState) => state.tasks);
  const { projects } = useSelector((state: RootState) => state.projects);
  const project = projects.find(p => p.id === id);

  const [activeTab, setActiveTab] = useState<'board' | 'list'>('board');

  useEffect(() => {
    if (id) {
      dispatch(fetchTasksByProject(id));
    }
  }, [dispatch, id]);

  const columns = ['To Do', 'In Progress', 'Done'];

  const getTasksByStatus = (status: string) => {
    return tasks.filter(t => t.status === status);
  };

  return (
    <div className="space-y-6">
      {/* Breadcrumbs */}
      <div className="flex items-center gap-2 text-sm text-[var(--text-muted)]">
        <Link to="/dashboard" className="hover:text-[var(--primary)] transition-colors">Dashboard</Link>
        <ChevronLeft size={16} />
        <Link to="/projects" className="hover:text-[var(--primary)] transition-colors">Projects</Link>
        <ChevronLeft size={16} />
        <span className="text-[var(--text-main)] font-medium">{project?.name || 'Project Details'}</span>
      </div>

      <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
        <div className="flex items-center gap-4">
          <div className="w-12 h-12 rounded-xl bg-indigo-50 text-[var(--primary)] flex items-center justify-center shadow-sm">
            <Briefcase size={24} />
          </div>
          <div>
            <h2 className="text-2xl font-bold text-[var(--text-main)]">{project?.name}</h2>
            <p className="text-[var(--text-muted)] text-sm">{project?.description}</p>
          </div>
        </div>
        <div className="flex items-center gap-3">
          <button className="flex items-center gap-2 px-4 py-2 text-sm font-medium border border-[var(--border)] rounded-[var(--radius)] hover:bg-slate-50 transition-all">
            <Filter size={16} />
            Filters
          </button>
          <button className="flex items-center gap-2 px-6 py-2.5 bg-[var(--primary)] text-white font-bold rounded-[var(--radius)] hover:bg-[var(--primary-hover)] transition-all shadow-md">
            <Plus size={20} />
            New Task
          </button>
        </div>
      </div>

      <div className="flex items-center gap-6 border-b border-[var(--border)]">
        <button 
          onClick={() => setActiveTab('board')}
          className={`pb-4 px-2 text-sm font-bold transition-all border-b-2 ${activeTab === 'board' ? 'border-[var(--primary)] text-[var(--primary)]' : 'border-transparent text-[var(--text-muted)] hover:text-[var(--text-main)]'}`}
        >
          Board View
        </button>
        <button 
          onClick={() => setActiveTab('list')}
          className={`pb-4 px-2 text-sm font-bold transition-all border-b-2 ${activeTab === 'list' ? 'border-[var(--primary)] text-[var(--primary)]' : 'border-transparent text-[var(--text-muted)] hover:text-[var(--text-main)]'}`}
        >
          List View
        </button>
      </div>

      {activeTab === 'board' && (
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 h-[calc(100vh-320px)] min-h-[500px]">
          {columns.map(column => (
            <div key={column} className="bg-slate-50/50 rounded-[var(--radius)] flex flex-col p-4 border border-transparent">
              <div className="flex items-center justify-between mb-4">
                <div className="flex items-center gap-2">
                  <h3 className="font-bold text-sm uppercase tracking-wider text-[var(--text-muted)]">{column}</h3>
                  <span className="bg-white px-2 py-0.5 rounded-full text-xs font-bold border border-[var(--border)]">
                    {getTasksByStatus(column).length}
                  </span>
                </div>
                <button className="text-[var(--text-muted)] hover:text-[var(--text-main)]">
                  <MoreVertical size={16} />
                </button>
              </div>

              <div className="flex-1 overflow-y-auto space-y-4 pr-2">
                {getTasksByStatus(column).map(task => (
                  <div key={task.id} className="bg-white p-4 rounded-[var(--radius)] shadow-sm border border-[var(--border)] hover:shadow-md transition-all cursor-pointer group">
                    <div className="flex justify-between items-start mb-2">
                      <span className="px-2 py-0.5 rounded-full bg-slate-100 text-[10px] font-bold text-[var(--text-muted)] uppercase tracking-wider">
                        {project?.name.split(' ')[0]}
                      </span>
                      <button className="text-[var(--text-muted)] opacity-0 group-hover:opacity-100 transition-opacity">
                        <MoreVertical size={14} />
                      </button>
                    </div>
                    <h4 className="font-bold text-sm mb-2">{task.title}</h4>
                    <p className="text-xs text-[var(--text-muted)] line-clamp-2 mb-4">
                      {task.description}
                    </p>
                    
                    <div className="flex items-center justify-between mt-auto pt-4 border-t border-slate-50">
                      <div className="flex items-center gap-3 text-[var(--text-muted)]">
                        <div className="flex items-center gap-1 text-[10px] font-bold">
                          <MessageSquare size={12} />
                          <span>{task.comments?.length || 0}</span>
                        </div>
                        <div className="flex items-center gap-1 text-[10px] font-bold">
                          <CheckSquare size={12} />
                          <span>0/1</span>
                        </div>
                      </div>
                      <div className="w-6 h-6 rounded-full bg-[var(--primary)] flex items-center justify-center text-[10px] text-white font-bold">
                        {task.assignee?.[0] || 'U'}
                      </div>
                    </div>
                  </div>
                ))}
                
                <button className="w-full py-2 border-2 border-dashed border-[var(--border)] rounded-[var(--radius)] text-xs font-bold text-[var(--text-muted)] hover:border-[var(--primary)] hover:text-[var(--primary)] transition-all flex items-center justify-center gap-2">
                  <Plus size={14} />
                  Add Task
                </button>
              </div>
            </div>
          ))}
        </div>
      )}

      {activeTab === 'list' && (
        <div className="bg-white rounded-[var(--radius)] border border-[var(--border)] overflow-hidden">
          <table className="w-full text-left">
            <thead className="bg-slate-50 text-xs font-bold text-[var(--text-muted)] uppercase tracking-wider border-b border-[var(--border)]">
              <tr>
                <th className="px-6 py-4">Task Name</th>
                <th className="px-6 py-4">Status</th>
                <th className="px-6 py-4">Assignee</th>
                <th className="px-6 py-4 text-right">Actions</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-[var(--border)]">
              {tasks.map(task => (
                <tr key={task.id} className="hover:bg-slate-50/50 transition-colors">
                  <td className="px-6 py-4">
                    <p className="font-bold text-sm">{task.title}</p>
                    <p className="text-xs text-[var(--text-muted)] line-clamp-1">{task.description}</p>
                  </td>
                  <td className="px-6 py-4">
                    <span className={`px-2 py-1 rounded-full text-[10px] font-bold uppercase tracking-wider ${
                      task.status === 'Done' ? 'bg-emerald-50 text-emerald-600' :
                      task.status === 'In Progress' ? 'bg-blue-50 text-blue-600' :
                      'bg-slate-100 text-slate-500'
                    }`}>
                      {task.status}
                    </span>
                  </td>
                  <td className="px-6 py-4">
                    <div className="flex items-center gap-2">
                      <div className="w-6 h-6 rounded-full bg-[var(--primary)] flex items-center justify-center text-[10px] text-white font-bold">
                        {task.assignee?.[0] || 'U'}
                      </div>
                      <span className="text-xs font-medium">{task.assignee || 'Unassigned'}</span>
                    </div>
                  </td>
                  <td className="px-6 py-4 text-right">
                    <button className="text-[var(--text-muted)] hover:text-[var(--text-main)] transition-colors">
                      <MoreVertical size={18} />
                    </button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
};

export default ProjectDetails;
