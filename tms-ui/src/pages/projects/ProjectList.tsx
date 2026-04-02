import { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../../store';
import { fetchProjects } from '../../store/slices/projectSlice';
import { Briefcase, Plus, MoreVertical, CheckSquare } from 'lucide-react';
import { Link } from 'react-router-dom';

const ProjectList = () => {
  const dispatch = useDispatch<AppDispatch>();
  const { projects, loading } = useSelector((state: RootState) => state.projects);
  const auth = useSelector((state: RootState) => state.auth);

  useEffect(() => {
    if (auth.user?.clientCode) {
      dispatch(fetchProjects(auth.user.clientCode));
    }
  }, [dispatch, auth.user?.clientCode]);

  return (
    <div className="space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h2 className="text-2xl font-bold text-[var(--text-main)]">Projects</h2>
          <p className="text-[var(--text-muted)] mt-1">Manage and track your ongoing projects.</p>
        </div>
        <button className="flex items-center gap-2 px-6 py-2.5 bg-[var(--primary)] text-white font-bold rounded-[var(--radius)] hover:bg-[var(--primary-hover)] transition-all shadow-md">
          <Plus size={20} />
          Create Project
        </button>
      </div>

      {loading ? (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {[1, 2, 3].map(i => (
            <div key={i} className="h-48 bg-white rounded-[var(--radius)] border border-[var(--border)] animate-pulse"></div>
          ))}
        </div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {projects.map((project) => (
            <div key={project.id} className="bg-white rounded-[var(--radius)] border border-[var(--border)] shadow-sm hover:shadow-lg transition-all p-6 group">
              <div className="flex justify-between items-start mb-4">
                <div className="w-12 h-12 rounded-xl bg-indigo-50 text-[var(--primary)] flex items-center justify-center">
                  <Briefcase size={24} />
                </div>
                <button className="text-[var(--text-muted)] hover:text-[var(--text-main)] transition-all">
                  <MoreVertical size={20} />
                </button>
              </div>

              <Link to={`/projects/${project.id}`}>
                <h3 className="text-lg font-bold group-hover:text-[var(--primary)] transition-colors line-clamp-1">
                  {project.name}
                </h3>
              </Link>
              <p className="text-[var(--text-muted)] text-sm mt-2 line-clamp-2 h-10">
                {project.description || 'No description provided.'}
              </p>

              <div className="mt-6 pt-6 border-t border-[var(--border)] flex items-center justify-between">
                <div className="flex -space-x-2">
                  {[1, 2, 3].map(i => (
                    <div key={i} className="w-8 h-8 rounded-full border-2 border-white bg-slate-200 flex items-center justify-center text-[10px] font-bold">
                      U{i}
                    </div>
                  ))}
                  <div className="w-8 h-8 rounded-full border-2 border-white bg-slate-50 flex items-center justify-center text-[10px] font-bold text-[var(--text-muted)]">
                    +2
                  </div>
                </div>
                
                <div className="flex items-center gap-4 text-[var(--text-muted)]">
                  <div className="flex items-center gap-1 text-xs">
                    <CheckSquare size={14} />
                    <span>24/32</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>
      )}

      {!loading && projects.length === 0 && (
        <div className="bg-white border-2 border-dashed border-[var(--border)] rounded-[var(--radius)] p-12 text-center">
          <div className="w-16 h-16 bg-slate-50 text-[var(--text-muted)] rounded-full flex items-center justify-center mx-auto mb-4">
            <Briefcase size={32} />
          </div>
          <h3 className="text-lg font-bold mb-1">No projects found</h3>
          <p className="text-[var(--text-muted)] mb-6">Get started by creating your first project.</p>
          <button className="inline-flex items-center gap-2 px-6 py-2.5 bg-[var(--primary)] text-white font-bold rounded-[var(--radius)] hover:bg-[var(--primary-hover)] transition-all">
            <Plus size={20} />
            Create Project
          </button>
        </div>
      )}
    </div>
  );
};

export default ProjectList;
